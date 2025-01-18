package com.example.apidemo.gtp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GtpService {
    @Value ("${openai.api.key}")
    private static String apiKey;

    private static final String API_URL = "https://api.gtpapi.com/2.0/";

    private final String model = "gpt-4o-mini";

    private final ObjectMapper mapper;
}




