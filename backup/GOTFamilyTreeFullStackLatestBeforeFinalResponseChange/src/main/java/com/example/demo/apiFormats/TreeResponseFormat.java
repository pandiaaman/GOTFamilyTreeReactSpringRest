package com.example.demo.apiFormats;

import java.util.List;

import com.example.demo.dto.CharacterResponse;
import com.example.demo.dto.CharacterWithChildrenResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreeResponseFormat {

	private String name;
	
	private CharacterResponse attributes;
	
	private List<CharacterWithChildrenResponse> children;
}
