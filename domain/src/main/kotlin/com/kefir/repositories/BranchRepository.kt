package com.kefir.repositories

import com.kefir.entities.Branch
import org.springframework.data.jpa.repository.JpaRepository

interface BranchRepository : JpaRepository<Branch, Long>
