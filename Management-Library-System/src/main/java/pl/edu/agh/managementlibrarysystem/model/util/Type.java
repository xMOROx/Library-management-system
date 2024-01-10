package pl.edu.agh.managementlibrarysystem.model.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.Arrays;
@Getter

public enum Type {
    BOOKS_ARE_DUE("Books are due"),
    RETURN_DATE_IS_CLOSE("Return date is approaching"),
    NEW_BOOKS_ARE_AVAILABLE("New books are available"),
    NO_RECENT_ACTIVITY("No recent activity");
    private final String type;
    Type(String type){
        this.type = type;
    }
    public static ObservableList<String> getObservableList(){
        return FXCollections.observableList(
                Arrays.stream(Type.values()).map(Type::getType).toList()
        );
    }

}
