package com.fs.sdk;

import com.fs.sdk.auth.FSCredential;
import com.fs.sdk.model.DeleteObjectsRequest;
import com.fs.sdk.model.GetObjectRequest;
import com.fs.sdk.model.PutObjectRequest;
import com.fs.sdk.model.PutObjectResponse;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class StorageServiceTest {

  private static final String ID = "cab220b3-aa4d-4e2c-ac62-3edeafe2b9c1";
  private static final String SECRET = "Z7IfQjmWkBceBxqz7lay7GkURpNHmGuvjezujvHU";

  @Test
  void testAuthenticate() {
    assertThatNoException().isThrownBy(this::initiateService);
  }

  @Test
  void testPutObject() throws URISyntaxException {
    File file = new File(this.getClass().getClassLoader().getResource("test.jpg").toURI());
    PutObjectRequest request = new PutObjectRequest("img", "pet/dog.jpg", file);
    StorageService service = initiateService();

    PutObjectResponse response = service.putObject(request);

    assertThat(response.getId()).isNotNull();
    assertThat(response.getKey()).isEqualTo("pet/dog.jpg");
    assertThat(response.getResult()).isNotBlank();
    assertThat(response.getCreatedAt()).isNotNull();
    assertThat(response.getLastModified()).isNotNull();
    assertThat(response.getLatestVersion()).isNotNull();
  }

  @Test
  void testGetObject() {
    GetObjectRequest request = new GetObjectRequest(3, "image/cat2.jpg");
    StorageService service = initiateService();

    File file = service.getObject(request);

    assertThat(file).isNotEmpty();
    assertThat(file.getTotalSpace()).isGreaterThan(0);
    assertThat(file.getName()).isEqualTo("cat2.jpg");
    System.out.println(file.getAbsolutePath());
  }

  @Test
  void testDeleteObjects() throws URISyntaxException {
    StorageService service = initiateService();
    File file = new File(this.getClass().getClassLoader().getResource("test.jpg").toURI());
    String workspace = "img";
    String key = "pet/dog.jpg";
    service.putObject(new PutObjectRequest(workspace, key, file));

    DeleteObjectsRequest request = new DeleteObjectsRequest(workspace, List.of(key));

    assertThatNoException()
        .isThrownBy(
            () -> {
              service.deleteObjects(request);
            });
  }

  private StorageService initiateService() {
    FSCredential credential =
        new FSCredential(
            "cab220b3-aa4d-4e2c-ac62-3edeafe2b9c1",
            "Z7IfQjmWkBceBxqz7lay7GkURpNHmGuvjezujvHU",
            "localhost",
            8055);
    return new StorageService(credential);
  }
}