package com.knoema.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.knoema.meta.DataItemDetail;
import com.knoema.meta.DataItemMeasure;
import com.knoema.meta.DataItemTime;
import com.knoema.meta.DataItemValue;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class DataItemValueDeserializer extends StdDeserializer<DataItemValue> {
    public DataItemValueDeserializer() {
        super(DataItemValue.class);
    }

    @Override
    public DataItemValue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
            ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
            TreeNode treeRoot = mapper.readTree(jsonParser);
            if (treeRoot instanceof ObjectNode) {
                ObjectNode root = (ObjectNode) treeRoot;
                boolean hasDate = false;
                boolean hasFrequency = false;
                boolean hasValue = false;
                boolean hasUnit = false;
                for (Iterator<Map.Entry<String, JsonNode>> it = root.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> element = it.next();
                    switch (element.getKey()) {
                        case "date":
                            hasDate = true;
                            break;
                        case "frequency":
                            hasFrequency = true;
                            break;
                        case "value":
                            hasValue = true;
                            break;
                        case "unit":
                            hasUnit = true;
                            break;
                    }
                }

                Class<? extends DataItemValue> itemClass =
                        (hasDate && hasFrequency) ? DataItemTime.class :
                                (hasValue && hasUnit) ? DataItemMeasure.class :
                                        hasValue ? DataItemDetail.class : null;

                if (itemClass != null)
                    return mapper.treeToValue(root, itemClass);
            }
            return null;
        }

        String value = jsonParser.getValueAsString();
        DataItemDetail result = new DataItemDetail();
        result.value = value;
        return result;
    }
}
