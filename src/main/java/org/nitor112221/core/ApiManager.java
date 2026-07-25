package org.nitor112221.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nitor112221.dto.Contest;
import org.nitor112221.dto.Problem;
import org.nitor112221.dto.ProblemsResponse;
import org.nitor112221.dto.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ApiManager {
    private static final String baseURL = "https://codeforces.com/api/";

    public static ArrayList<Problem> loadProblems() {
        try (HttpClient client = HttpClient.newHttpClient()){
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseURL + "problemset.problems")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            Response<ProblemsResponse> problemsResponse = mapper.readValue(response.body(), new TypeReference<>() {});
            return problemsResponse.getResult().getProblems();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Contest> loadContests() {
        try (HttpClient client = HttpClient.newHttpClient()){
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseURL + "contest.list")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            Response<ArrayList<Contest>> problemsResponse = mapper.readValue(response.body(), new TypeReference<>() {});
            return problemsResponse.getResult();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
