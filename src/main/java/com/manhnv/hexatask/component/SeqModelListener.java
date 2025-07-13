package com.manhnv.hexatask.component;

import com.manhnv.hexatask.model.sequence.Sequenceable;
import com.manhnv.hexatask.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeqModelListener extends AbstractMongoEventListener<Sequenceable> {
    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Sequenceable> event) {
        Sequenceable source = event.getSource();
        if (source.getId() == null || source.getId() < 1) {
            String sequenceName = getSequenceName(source.getClass());
            source.setId(sequenceGeneratorService.generateSequence(sequenceName));
        }
    }

    private String getSequenceName(Class<?> clazz) {
        try {
            return (String) clazz.getField("SEQUENCE_NAME").get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("SEQUENCE_NAME not found in " + clazz.getSimpleName());
        }
    }
}
