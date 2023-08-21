package com.hba.reservationservice.domain.account;

import com.fs.sdk.StorageService;
import com.fs.sdk.model.DeleteObjectsRequest;
import com.fs.sdk.model.PutObjectRequest;
import com.fs.sdk.model.PutObjectResponse;
import com.hba.hbacore.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountService implements UserDetailsService {

  private final AccountRepository accountRepository;
  private final StorageService storageService;

  @Transactional(readOnly = true)
  public Account findById(int id) {
    return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Account account =
        accountRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Email not found " + email));
    User user = new User();
    user.setId(account.getId());
    user.setEmail(email);
    user.setPassword(account.getPassword());
    user.setAuthority(account.getAuthority().name());
    return user;
  }

  public Account updateAccount(User user, UserInfoUpdate infoUpdate, MultipartFile image) {
    Account account = findById(user.getId());
    account.setName(infoUpdate.getName());

    if (image != null) {
      try {
        // delete old image
        DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest("upload_img", List.of(account.getImageUrl()));
        storageService.deleteObjects(deleteRequest);

        // update new image
        Path tempPath = Files.createTempFile("partnerservice", null);
        Files.copy(image.getInputStream(), tempPath, StandardCopyOption.REPLACE_EXISTING);
        PutObjectRequest putRequest =
            new PutObjectRequest(
                "upload_img", account.getImageUrl(), tempPath.toFile());
        PutObjectResponse response = storageService.putObject(putRequest);
        log.info("Update image successfully with response {}", response);

        Files.deleteIfExists(tempPath);
      } catch (IOException e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail to update image");
      }
    }
    return accountRepository.save(account);
  }
}