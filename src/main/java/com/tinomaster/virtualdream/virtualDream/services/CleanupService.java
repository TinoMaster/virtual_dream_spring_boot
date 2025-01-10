package com.tinomaster.virtualdream.virtualDream.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CleanupService {

	private final JdbcTemplate jdbcTemplate;

	/**
	 * Elimina registros donde `businessFinalSale` es NULL y han pasado más de un
	 * día.
	 */
	public int eliminarRegistrosAntiguos() {
		String sql = """
				DELETE FROM service_sale
				WHERE business_final_sale_id IS NULL
				  AND created_at < NOW() - INTERVAL '1 day'
				""";
		return jdbcTemplate.update(sql);
	}
}
