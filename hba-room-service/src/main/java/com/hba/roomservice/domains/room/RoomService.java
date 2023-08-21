package com.hba.roomservice.domains.room;

import com.hba.hbacore.util.StringUtil;
import com.hba.roomservice.api.domain.SuiteDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

  private static final Comparator<RentableSuiteDTO> SORT_SUITE_ON_OCCUPATION_ASC =
      Comparator.comparing(RentableSuiteDTO::getOccupation);
  private static final Comparator<RoomRent> SORT_OCCUPATION_DESC =
      Comparator.comparing(RoomRent::getOccupation, Comparator.reverseOrder());
  private static final Comparator<RentableSuiteDTO> SORT_SUITE_BY_PRICE_ASC =
      Comparator.comparingInt(RentableSuiteDTO::getOriginalPrice);
  private static final Comparator<RoomRent> SORT_BY_INDEX_ASC =
      Comparator.comparingInt(RoomRent::getIndex);

  private final JpaSuiteRepository jpaSuiteRepository;
  private final SuiteTypeRepository suiteTypeRepository;
  private final SuiteStyleRepository suiteStyleRepository;
  private final AmenityRepository amenityRepository;
  private final MongoTemplate mongoTemplate;

  private static int searchSuiteMatchOccupation(final int[] suiteOccupations, final RoomRent item) {
    int l = 0;
    int h = suiteOccupations.length - 1;
    int mid;
    while (l < h) {
      mid = l + (h - l) / 2;
      if (item.getOccupation() <= suiteOccupations[mid]) h = mid;
      else l = mid + 1;
    }
    return l;
  }

  @Transactional(readOnly = true)
  public List<SuiteType> getSuiteTypes() {
    return suiteTypeRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<SuiteStyle> getSuiteStyles() {
    return suiteStyleRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Amenity> getAmenities() {
    return amenityRepository.findAll();
  }

  @Transactional(readOnly = true)
  public void updateSuiteSearchView() {
    List<SuiteDTO> suites =
        jpaSuiteRepository.getSuiteSearchView().stream()
            .map(SuiteDTO::new)
            .collect(Collectors.toList());
    for (SuiteDTO suite : suites) {
      mongoTemplate.save(suite, "suites");
    }
  }

  @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
  public Page<SuiteDTO> findSuites(SuiteSearchRequest request, Pageable pageable) {
    Query query = new Query().with(pageable);

    String typeIds = request.getTypeIds();
    if (StringUtils.hasText(typeIds)) {
      query.addCriteria(Criteria.where("typeId").in(StringUtil.underscoreDelimiterToList(typeIds)));
    }
    String styleIds = request.getStyleIds();
    if (StringUtils.hasText(styleIds)) {
      query.addCriteria(
          Criteria.where("styleId").in(StringUtil.underscoreDelimiterToList(styleIds)));
    }

    Boolean hasPromotion = request.getHasPromotion();
    if (hasPromotion != null && hasPromotion) {
      query.addCriteria(Criteria.where("discountId").isNull().not());
    }

    Integer priceFrom = request.getPriceFrom();
    Integer priceTo = request.getPriceTo();
    query.addCriteria(
        Criteria.where("actualPrice")
            .gte(priceFrom != null ? priceFrom : 0)
            .lte(priceTo != null ? priceTo : 10_000_000));

    List<SuiteDTO> suites = mongoTemplate.find(query, SuiteDTO.class, "suites");
    long count = mongoTemplate.count(query.skip(-1).limit(-1), SuiteDTO.class, "suites");
    return new PageImpl<>(suites, pageable, count);
  }

  @Transactional(readOnly = true)
  public SuiteDTO getSuiteInfo(int suiteId) {
    SuiteDTO suite = mongoTemplate.findById(suiteId, SuiteDTO.class, "suites");
    if (suite == null) {
      throw new SuiteNotFoundException(suiteId);
    }
    return suite;
  }

  @Transactional(readOnly = true)
  public boolean isRentableSuiteMappingValid(
      LocalDate checkinAt, LocalDate checkoutAt, Map<Integer, Integer> suiteIdMapping) {
    Map<Integer, Integer> idWithRoomNum = new HashMap<>();
    for (RentableSuiteRecordProjection record :
        jpaSuiteRepository.findRentableSuitesByDate(checkinAt, checkoutAt)) {
      idWithRoomNum.put(record.getSuiteId(), record.getEmptyRoomNum());
    }

    for (Map.Entry<Integer, Integer> entry : suiteIdMapping.entrySet()) {
      int suiteId = entry.getKey();
      int roomNum = entry.getValue();

      if (!idWithRoomNum.containsKey(suiteId) || idWithRoomNum.get(suiteId) < roomNum) {
        log.info(
            "Verifying invalid for suite id {}, result: [exist: {}, room num desired '{}' and actual '{}']",
            suiteId,
            idWithRoomNum.containsKey(suiteId),
            roomNum,
            idWithRoomNum.get(suiteId));
        return false;
      }
    }

    return true;
  }

  @Transactional(readOnly = true)
  public RentableSuiteSearchResponse findRentableSuites(
      LocalDate checkinAt, LocalDate checkoutAt, List<Integer> occupations) {

    List<RentableSuiteRecordProjection> idWithRoomNumPairs =
        jpaSuiteRepository.findRentableSuitesByDate(checkinAt, checkoutAt);

    List<RentableSuiteDTO> availableSuites = new ArrayList<>();
    for (RentableSuiteRecordProjection idWithRoomNum : idWithRoomNumPairs) {
      RentableSuiteDTO suite =
          mongoTemplate.findOne(
              Query.query(Criteria.where("id").is(idWithRoomNum.getSuiteId())),
              RentableSuiteDTO.class,
              "suites");
      suite.setEmptyRoomNum(idWithRoomNum.getEmptyRoomNum());
      availableSuites.add(suite);
    }

    availableSuites.sort(SORT_SUITE_ON_OCCUPATION_ASC);

    // Use record to keep index
    List<RoomRent> roomRents = new ArrayList<>();
    for (int i = 0; i < occupations.size(); i++) {
      roomRents.add(new RoomRent(i, occupations.get(i)));
    }

    // Sort occupations desc
    roomRents.sort(SORT_OCCUPATION_DESC);

    int totalSuites = availableSuites.size();
    final int[] suiteOccupations = new int[totalSuites];
    final int[] emptyRoomNums = new int[totalSuites];

    for (int i = 0; i < availableSuites.size(); i++) {
      suiteOccupations[i] = availableSuites.get(i).getOccupation();
      emptyRoomNums[i] = availableSuites.get(i).getEmptyRoomNum();
    }

    for (final RoomRent roomRent : roomRents) {
      int index = searchSuiteMatchOccupation(suiteOccupations, roomRent);
      if (roomRent.getOccupation() <= suiteOccupations[index]) {
        for (int suiteIndex = index; suiteIndex < totalSuites; suiteIndex++) {
          if (emptyRoomNums[suiteIndex] > 0) {
            roomRent.getSuites().add(availableSuites.get(suiteIndex));
            emptyRoomNums[suiteIndex]--;
          }
        }
      }
    }

    // Reorder `roomRents` array to the original state
    roomRents.sort(SORT_BY_INDEX_ASC);

    // Fill result with the selected suites
    List<List<Integer>> occupationMapSuiteIds = new ArrayList<>();
    List<RentableSuiteDTO> rentableSuites = new ArrayList<>();

    // Sort the list of selected suite of each required room by price
    for (final RoomRent item : roomRents) {
      final List<RentableSuiteDTO> selectedSuites =
          item.getSuites().stream().sorted(SORT_SUITE_BY_PRICE_ASC).collect(Collectors.toList());
      rentableSuites.addAll(selectedSuites);
      occupationMapSuiteIds.add(
          selectedSuites.stream().map(RentableSuiteDTO::getId).collect(Collectors.toList()));
    }

    return new RentableSuiteSearchResponse(occupationMapSuiteIds, rentableSuites);
  }

  @Getter
  @ToString
  private static class RoomRent {
    private final int index;
    private final int occupation;
    private final Set<RentableSuiteDTO> suites = new HashSet<>();

    public RoomRent(int index, int occupation) {
      this.index = index;
      this.occupation = occupation;
    }
  }
}