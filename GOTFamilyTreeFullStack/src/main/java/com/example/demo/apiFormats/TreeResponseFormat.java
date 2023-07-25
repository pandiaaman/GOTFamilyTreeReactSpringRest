package com.example.demo.apiFormats;

import java.util.List;


import com.example.demo.dto.CharacterWithChildrenResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TreeResponseFormat {

	private String name;
	
	private CharacterWithChildrenResponse attributes;
	
	private List<CharacterWithChildrenResponse> children;
}
