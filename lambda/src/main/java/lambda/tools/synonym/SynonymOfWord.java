package lambda.tools.synonym;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lambda.Handler;
import lambda.Response;
import lambda.tools.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class SynonymOfWord extends Handler<String> {
    @Override
    public Response handleRequest(String input, Context context) {
        Tools tools = new Tools();
        List<String> result = new ArrayList<>();
        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readTree(tools.getSynonyms(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(jsonNode != null){
            List<JsonNode> list = jsonNode.get("def").findValues("syn");
            result = list.get(0).findValuesAsText("text");
        }
        return new Response(200, result);
    }
}
