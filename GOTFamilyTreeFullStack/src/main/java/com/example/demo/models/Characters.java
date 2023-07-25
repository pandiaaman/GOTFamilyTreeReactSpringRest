package com.example.demo.models;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name="character_table")
public class Characters {
	
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	private String characterId;
	
	private String characterName;
	
	private String houseName;
	
	private List<String> parents;
	
	private List<String> siblings;
	
	private List<String> parentOf;
	
	private List<String> marriedEngaged;
	
	@ColumnDefault("false")
	private boolean isFavourite;
	
	private String characterImageThumb;
	
	private String characterImageFull;
	
	private String characterLink;
	
	private String actorName;
	
	private String actorLink;
	
	private String nickname;
	
	@ColumnDefault("false")
	private boolean royal;
	
	private List<String> killed;
	
	private List<String> killedBy;
	
	
}
