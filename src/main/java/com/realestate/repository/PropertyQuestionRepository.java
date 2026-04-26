package com.realestate.repository;

import com.realestate.model.PropertyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropertyQuestionRepository extends JpaRepository<PropertyQuestion, Long> {
    List<PropertyQuestion> findByPropertyId(Long propertyId);
    List<PropertyQuestion> findByIsAnswered(Boolean isAnswered);
}
