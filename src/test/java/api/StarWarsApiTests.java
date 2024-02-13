package api;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import model.Film;
import model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.get;


public class StarWarsApiTests {
	@BeforeEach
	public void setup() {
		RestAssured.baseURI = "https://swapi.dev";
	}

	@Test
	public void testingTask2() {
		Film latestFilm = findLatestFilm();
		System.out.println("Latest film: " + latestFilm.getTitle());
		Assertions.assertEquals("Revenge of the Sith", latestFilm.getTitle());

		String tallestCharacterInFilm = findTallestCharacterInFilm(latestFilm);
		System.out.println("Tallest character in film: " + tallestCharacterInFilm);
		Assertions.assertEquals("Tarfful", tallestCharacterInFilm);

		String tallestCharacterEver = findTallestCharacterEver();
		System.out.println("Tallest character ever: " + tallestCharacterEver);
		Assertions.assertEquals("Yarael Poof", tallestCharacterEver);

		validatePeopleApiSchema();
	}


	public Film findLatestFilm() {
		Response response = get("/api/films/");
		List<Film> films = response.jsonPath().getList("results", Film.class);

		Film latestFilm = films.stream()
				.max(Comparator.comparing(Film::getReleaseDate))
				.orElseThrow(() -> new IllegalStateException("Cannot find film with latest release date"));

		return latestFilm;
	}
	

	public String findTallestCharacterInFilm(Film film) {
		String tallestCharacterName = "";
		int maxHeight = 0;

		for (String characterUrl : film.getCharacters()) {
			Response response = get(characterUrl);
			int height = response.jsonPath().getInt("height");
			if (height > maxHeight) {
				maxHeight = height;
				tallestCharacterName = response.jsonPath().getString("name");
			}
		}

		return tallestCharacterName;
	}

	public String findTallestCharacterEver() {
		int page = 1;
		String tallestCharacterName = "";
		int maxHeight = 0;

		while (true) {
			Response response = get("/api/people/?page=" + page);
			List<Person> people = response.jsonPath().getList("results", Person.class);
			if (people.isEmpty()) {
				break;
			}

			for (Person person : people) {
				int height = !person.getHeight().equals("unknown") ? Integer.parseInt(person.getHeight()) : 0;
				if (height > maxHeight) {
					maxHeight = height;
					tallestCharacterName = person.getName();
				}
			}

			page++;
		}

		return tallestCharacterName;
	}

	public void validatePeopleApiSchema() {
		RestAssured.given()
				.when()
				.get("https://swapi.dev/api/people/schema/")
				.then()
				.assertThat()
				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("people-schema.json"));
	}
}
