package com.knoema.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.knoema.meta.CalculatedMember;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PivotMemberListDeserializer extends JsonDeserializer< List<Object> > {
    @Override
    public List<Object> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (jsonParser.currentToken() == JsonToken.START_ARRAY) {
            ArrayList<Object> result = new ArrayList<>();
            do {
                switch (jsonParser.nextToken()) {
                    case END_ARRAY:
                        return result;
                    case VALUE_NUMBER_INT:
                        result.add(jsonParser.getIntValue());
                        break;
                    case START_OBJECT:
                        result.add(jsonParser.readValueAs(CalculatedMember.class));
                        break;
                    default:
                        result.add(jsonParser.readValueAs(String.class));
                        break;
                }
            }
            while (true);
        }
        return null;
    }

}
