package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Film {
	private String title;
	private Date releaseDate;
	int episodeId;
	private String openingCrawl;
	private String director;
	private String producer;

	private List<String> characters;
	private List<String> planets;
	private List<String> starships;
	private List<String> vehicles;
	private List<String> species;
	private Date created;
	private Date edited;
	private String url;
}
