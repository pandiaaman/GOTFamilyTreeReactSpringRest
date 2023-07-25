package com.example.demo.dto;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class CharacterWithChildrenResponse {

private String characterId;
	
	
	private String characterName;
	
	private CharacterResponse attributes;
	
	private List<CharacterWithChildrenResponse> parentOf;
	
	
	
	
	//getter
	@JsonProperty("name")
	public String getCharacterName() {
		return characterName;
	}
	
	@JsonProperty("attributes")
	public CharacterResponse getAttributes() {
		return attributes;
	}
	
	@JsonProperty("children")
	public List<CharacterWithChildrenResponse> getParentOf(){
		return parentOf;
	}
	
}
