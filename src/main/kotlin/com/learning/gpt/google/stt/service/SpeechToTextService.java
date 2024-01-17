package com.learning.gpt.google.stt.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.learning.gpt.aop.ExeTimer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpeechToTextService {

    @Value("${google.credentials.path}")
    private String googleCredentialsPath;

    @ExeTimer
    public String transcribe(MultipartFile audioFile) {
        GoogleCredentials credentials;
        try (InputStream serviceAccountStream = new FileInputStream(googleCredentialsPath)) {
            credentials = GoogleCredentials.fromStream(serviceAccountStream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException e) {
            throw new RuntimeException("Error loading service account key.", e);
        }

        SpeechSettings settings;
        try {
            // 오디오 파일을 byte array로 decode
            byte[] audioBytes = audioFile.getBytes();
            settings = SpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            // 클라이언트 인스턴스화
            try (SpeechClient speechClient = SpeechClient.create(settings)) {
                // 오디오 객체 생성
                ByteString audioData = ByteString.copyFrom(audioBytes);
                RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                        .setContent(audioData)
                        .build();

                // 설정 객체 생성
                RecognitionConfig recognitionConfig =
                        RecognitionConfig.newBuilder()
                                .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                                .setSampleRateHertz(48000)
                                .setAudioChannelCount(2)
                                .setLanguageCode("ko-KR")
                                .build();

                // 오디오-텍스트 변환 수행
                RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);
                List<SpeechRecognitionResult> results = response.getResultsList();

                if (!results.isEmpty()) {
                    // 주어진 말 뭉치에 대해 여러 가능한 스크립트를 제공. 0번(가장 가능성 있는)을 사용한다.
                    SpeechRecognitionResult result = results.get(0);
                    return result.getAlternatives(0).getTranscript();
                } else {
                    log.error("No transcription result found");
                    return "";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Required part 'audioFile' is not present.");
        }
    }
}
