package lambda.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import lambda.test.model.CloseQuestion;
import lambda.test.model.OpenQuestion;
import lambda.test.model.Test;
import lambda.test.model.ValueQuestion;
import lambda.tools.translator.TranslateResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tools {
    private Test test;
    private String lang = "";

    public Tools() {

    }

    public Tools(Test test) {
        this.test = test;
        if (this.test.getLanguage().equals("pl") || this.test.getLanguage().equals("PL")) {
            this.lang = "&lang=pl-en";
            this.test.setLanguage("en");
        } else {
            this.lang = "&lang=en-pl";
            this.test.setLanguage("pl");
        }
    }

    //region [Translator]
    public Test translateTest() {
        this.test.setTitle(translateText(this.test.getTitle()));
        this.test.setCloseQuestions(translateCloseQuestions(this.test.getCloseQuestions()));
        this.test.setOpenQuestions(translateOpenQuestions(this.test.getOpenQuestions()));
        this.test.setValueQuestions(translateValueQuestions(this.test.getValueQuestions()));

        return this.test;
    }

    private String translateText(String input) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TranslateResponse translateResponse = objectMapper.readValue(postRequest(input), TranslateResponse.class);
            return translateResponse.getText().get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //endregion

    //region [Synonym searcher]
    public String getSynonyms(String input) {
        return getRequest(input);
    }
    //endregion

    //region [Tools]
    private static String streamToString(InputStream inputStream) {
        return new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
    }
    //endregion

    //region [Connection and request]
    private HttpURLConnection createPostConnection(String input) throws IOException {
        URL obj = new URL(input);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("charset", "utf-8");
        con.connect();
        return con;
    }

    private HttpURLConnection createGetConnection(String input) throws IOException {
        URL url = new URL(input);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        return con;
    }

    private String getRequest(String input) {
        String result = null;
        try {
            String yandexDicKey = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20200116T213001Z.6be5317691bae1ff.7faedf456380aa760d1a473b3a50be12d04d622d&lang=en-en&text=";
            InputStream inputStream = createGetConnection((yandexDicKey + input).replace(" ", "%20")).getInputStream();
            result = streamToString(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private String postRequest(String input) {
        String result = null;
        try {
            String yandexKey = "trnsl.1.1.20200108T191910Z.fe657624420b3a8c.9b1c3b15e8688d96a425d4596dfc2c6321f04ee2";
            String translateUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=";
            String url = translateUrl + yandexKey + "&text=";
            InputStream inputStream = createPostConnection((url + input + this.lang).replace(" ", "%20")).getInputStream();
            result = streamToString(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    //endregion

    //region [Questions Translator]
    private List<CloseQuestion> translateCloseQuestions(List<CloseQuestion> closeQuestions) {
        for (CloseQuestion closeQuestion : closeQuestions) {
            closeQuestion.setQuestion(translateText(closeQuestion.getQuestion()));

            for (int j = 0; j < closeQuestion.getCorrectAnswers().size(); j++) {
                ArrayList<String> correctAnswers = new ArrayList<>();
                correctAnswers.add(translateText(closeQuestion.getCorrectAnswers().get(j)));
                closeQuestion.setCorrectAnswers(correctAnswers);
            }

            for (int j = 0; j < closeQuestion.getIncorrectAnswers().size(); j++) {
                ArrayList<String> incorrectAnswer = new ArrayList<>();
                incorrectAnswer.add(translateText(closeQuestion.getIncorrectAnswers().get(j)));
                closeQuestion.setIncorrectAnswers(incorrectAnswer);
            }
        }
        return closeQuestions;
    }

    private List<OpenQuestion> translateOpenQuestions(List<OpenQuestion> openQuestions) {
        for (OpenQuestion openQuestion : openQuestions) {
            openQuestion.setQuestion(translateText(openQuestion.getQuestion()));
            openQuestion.setCorrectAnswer(translateText(openQuestion.getCorrectAnswer()));
        }
        return openQuestions;
    }

    private List<ValueQuestion> translateValueQuestions(List<ValueQuestion> valueQuestions) {
        for (ValueQuestion valueQuestion : valueQuestions) {
            valueQuestion.setQuestion(translateText(valueQuestion.getQuestion()));
        }
        return valueQuestions;
    }
    //endregion
}
