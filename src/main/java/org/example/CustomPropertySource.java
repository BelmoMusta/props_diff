package org.example;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.File;
import java.io.IOException;

public class CustomPropertySource extends ResourcePropertySource {
    public CustomPropertySource(String location) throws IOException {
        super(new FileSystemResource(location));

    } public CustomPropertySource(File location) throws IOException {
        super(new FileSystemResource(location));

    }


}
