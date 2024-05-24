package com.example.cryptoinfo.data.repository;

import com.example.cryptoinfo.data.entity.CryptoData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoDataRepository extends JpaRepository<CryptoData, Long> {
}
