package il.cshaifasweng.OCSFMediatorExample.client.util;

import il.cshaifasweng.OCSFMediatorExample.client.util.comboBox.AutocompleteComboBox;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.List;
import java.util.Set;

public class MovieFeatureManager {
    private final List<Movie> movies;
    private final ObservableList<Movie> filteredMovies;

    public MovieFeatureManager(List<Movie> movies) {
        this.movies = movies;
        this.filteredMovies = FXCollections.observableArrayList(movies);
    }

    public <T> void initializeComboBox(ComboBox<T> comboBox, Set<T> items) {
        comboBox.setItems(FXCollections.observableArrayList(items));
        AutocompleteComboBox.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()));
    }

    public void filterMovies(
            ComboBox<Integer> colDuration,
            ComboBox<String> colEnglish,
            ComboBox<String> colGenre,
            ComboBox<Integer> colHVPrice,
            ComboBox<String> colHebrew,
            ComboBox<Integer> colId,
            ComboBox<Movie.StreamingType> colStreamingType,
            ComboBox<Integer> colTheaterPrice
    ) {
        filteredMovies.setAll(movies);

        filteredMovies.removeIf(movie ->
                (colDuration.getValue() != null && movie.getDuration()!=(colDuration.getValue())) ||
                        (colEnglish.getValue() != null && !movie.getEnglishName().toLowerCase().contains(colEnglish.getValue().toLowerCase())) ||
                        (colGenre.getValue() != null && !movie.getGenre().toLowerCase().contains(colGenre.getValue().toLowerCase())) ||
                        (colHVPrice.getValue() != null && movie.getHomeViewingPrice()!=(colHVPrice.getValue())) ||
                        (colHebrew.getValue() != null && !movie.getHebrewName().toLowerCase().contains(colHebrew.getValue().toLowerCase())) ||
                        (colId.getValue() != null && movie.getId()!=colId.getValue()) ||
                        (colStreamingType.getValue() != null && !movie.getStreamingType().equals(colStreamingType.getValue())) ||
                        (colTheaterPrice.getValue() != null && movie.getTheaterPrice()!=(colTheaterPrice.getValue()))
        );
    }

    public ObservableList<Movie> getFilteredMovies() {
        return filteredMovies;
    }
}
