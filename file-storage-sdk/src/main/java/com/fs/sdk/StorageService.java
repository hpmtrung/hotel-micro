package com.fs.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fs.sdk.auth.FSCredential;
import com.fs.sdk.model.*;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fs.sdk.JacksonUtil.deserializeJson;
import static com.fs.sdk.JacksonUtil.serializeJson;

public final class StorageService {
  public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
  public static final MediaType OCTET_STREAM = MediaType.get("application/octet-stream");
  private static final Logger log = Logger.getLogger(StorageService.class.getName());
  private static final Pattern FILE_NAME_PATTERN = Pattern.compile("filename=\"(.+)\"");
  private static Path localTempDir;

  static {
    try {
      localTempDir = Files.createTempDirectory("fileStorageTemp");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private final String connectionStr;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final FSCredential credential;
  private final OkHttpClient client = new OkHttpClient();
  private String token;

  public StorageService(FSCredential credential) {
    this.credential = credential;
    this.connectionStr = credential.getHost() + ":" + credential.getPort();
    log.info("Configuring with connection string " + connectionStr);
    authenticate();
  }

  private static String parseFileName(String str) {
    Matcher matcher = FILE_NAME_PATTERN.matcher(str);
    if (matcher.find()) {
      return matcher.group(1);
    } else {
      throw new StorageServiceException("Getting an object with invalid file name");
    }
  }

  public PutObjectResponse putObject(PutObjectRequest data) throws StorageServiceException {
    log.info("Preparing to put an object " + data);
    File file = data.getFile();
    RequestBody body =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            // .addFormDataPart("metadata", serializeJson(data.getMetadata()))
            .addFormDataPart("workspace", data.getWorkspace())
            .addFormDataPart("key", data.getKey())
            .addFormDataPart("file", file.getName(), RequestBody.create(file, OCTET_STREAM))
            .build();
    Request request = prepareRequest("/api/storage/object").put(body).build();
    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful() || response.body() == null) {
        throw new StorageServiceException(
            "Fail to put an object", response.body() != null ? response.body().string() : null);
      }
      PutObjectResponse result = deserializeJson(response.body().string(), PutObjectResponse.class);
      log.info("Putting successfully with response " + result);
      return result;
    } catch (IOException e) {
      throw new StorageServiceException("Error when putting an object", e);
    }
  }

  public File getObject(GetObjectRequest data) throws StorageServiceException {
    log.info("Preparing to get an object " + data);
    Request request =
        new Request.Builder()
            .url(
                connectionStr
                    + "/api/storage/object?workspaceId="
                    + data.getWorkspaceId()
                    + "&key="
                    + data.getKey())
            .build();
    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful() || response.body() == null) {
        throw new StorageServiceException(
            "Fail to get an object", response.body() != null ? response.body().string() : "null");
      }
      String fileName =
          Optional.ofNullable(response.header("Content-Disposition"))
              .map(StorageService::parseFileName)
              .orElseThrow(() -> new StorageServiceException("Cannot parse a file name"));

      Path path = localTempDir.resolve(Paths.get(fileName)).normalize().toAbsolutePath();

      Headers headers = response.headers();
      String metadata = headers.get("USER_METADATA");
      if (metadata != null) {
        ObjectMetadata om = deserializeJson(metadata, ObjectMetadata.class);
        Files.setLastModifiedTime(path, FileTime.fromMillis(om.getLastModified()));
      }

      try (InputStream inputStream = response.body().byteStream()) {
        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
      }

      return path.toFile();
    } catch (IOException e) {
      throw new StorageServiceException("Error when getting an object", e);
    }
  }

  public void deleteObjects(DeleteObjectsRequest data) throws StorageServiceException {
    log.info("Preparing to delete objects " + data);
    Request request =
        prepareRequest(
                "/api/storage/object?workspace="
                    + data.getWorkspace()
                    + "&keys="
                    + String.join(",", data.getKeys()))
            .delete()
            .build();
    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new StorageServiceException(
            "Fail to delete objects", response.body() != null ? response.body().string() : null);
      }
      log.info("Delete objects successfully.");
    } catch (IOException e) {
      throw new StorageServiceException("Error when deleting objects", e);
    }
  }

  private Request.Builder prepareRequest(String url) {
    return new Request.Builder()
        .header("Authorization", "Bearer " + token)
        .url(this.connectionStr + url);
  }

  private void authenticate() {
    RequestBody body = RequestBody.create(serializeJson(credential), JSON);
    Request request = new Request.Builder().url(connectionStr + "/api/auth").post(body).build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful() || response.body() == null) {
        throw new StorageServiceException(
            "Fail to get an authentication token from file service", response.message());
      } else {
        this.token = response.body().string();
        log.info("Get a token from file service successfully.");
      }
    } catch (IOException e) {
      throw new StorageServiceException("Fail to get an authentication token from file service", e);
    }
  }
}