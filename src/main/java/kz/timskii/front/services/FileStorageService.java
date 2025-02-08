package kz.timskii.front.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;


@Slf4j
@Service
public class FileStorageService {

    private final MinioClient minioClient;
    private final String bucketName;
    private final String url;

    public FileStorageService(MinioClient minioClient, @Value("${minio.bucketName}")String bucketName,
                              @Value("${minio.url}") String url) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
        this.url = url;
    }

    public String save(InputStream is, String filename, String contentType) throws Exception{
        minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filename)
                        .stream(is, -1, 10485760)
                        .contentType(contentType)
                .build());
        return url + "/" + bucketName + "/" + filename;
    }
}
