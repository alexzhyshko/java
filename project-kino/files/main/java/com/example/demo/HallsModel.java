package com.example.demo;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class HallsModel {
	@Autowired
	private JdbcTemplate connection;
	public HallsModel() {
	
	}
	public int[][] getHallDimensions(String seance){
		
		String query = "SELECT * FROM "+Tables.hallsTable()+" WHERE id=(SELECT hallid FROM "+Tables.seancesTable()+" WHERE id=?)";
		int[][] res;
		List<String> temp=connection.query(query, new Object[] {Integer.parseInt(seance)}, (rs,rowNum)->rs.getInt("line")+","+rs.getInt("colmns"));
		res = new int[Integer.parseInt(temp.get(1).split(",")[0])][Integer.parseInt(temp.get(1).split(",")[1])];
		return res;
	}
	
	public HashMap<Integer, String> getHalls() {
		HashMap<Integer, String> result = new HashMap<>();
		String query = "SELECT * FROM "+Tables.hallsTable();
		List<String> temp = connection.query(query, (rs,rowNum)->rs.getInt("id")+","+ rs.getString("name"));
		for(String str : temp) {
			result.put(Integer.parseInt(str.split(",")[0]), str.split(",")[1]);
		}
		return result;
		
	}
}
