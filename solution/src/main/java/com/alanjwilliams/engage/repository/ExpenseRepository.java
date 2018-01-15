package com.alanjwilliams.engage.repository;

import com.alanjwilliams.engage.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Expense JPA Repository. Extends {@link JpaRepository} which also gives basic CRUD methods.
 */
@Repository
public interface ExpenseRepository  extends JpaRepository<Expense, UUID> {
}
