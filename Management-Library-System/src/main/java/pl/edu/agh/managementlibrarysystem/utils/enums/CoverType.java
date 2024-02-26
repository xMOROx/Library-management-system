package pl.edu.agh.managementlibrarysystem.utils.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CoverType {
    SOFT("Soft"),
    HARD("Hard");

    private final String coverType;

    CoverType(String coverType) {
        this.coverType = coverType;
    }


    public static ObservableList<String> getObservableList() {
        return FXCollections.observableList(
                        Arrays.stream(CoverType.values())
                        .map(CoverType::getCoverType)
                        .toList()
                );
    }
}
