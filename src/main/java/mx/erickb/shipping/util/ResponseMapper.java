package mx.erickb.shipping.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.model.Describable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponseMapper {

    private final Logger logger = LoggerFactory.getLogger(ResponseMapper.class);
    private final ObjectMapper mapper;

    public ResponseMapper(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T extends Describable> List<String> map(String response, Class<T[]> valueType) throws InvalidResponseException {
        try {
            T[] items = mapper.readValue(response, valueType);
            return Arrays.stream(items)
                    .map(T::describe)
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            logger.error("Error when processing server response (" + e.getMessage() + ")");
            throw new InvalidResponseException("Invalid response: \"" + response + '"');
        }
    }
}
