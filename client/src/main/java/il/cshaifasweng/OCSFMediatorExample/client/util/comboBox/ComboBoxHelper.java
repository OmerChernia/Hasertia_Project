package il.cshaifasweng.OCSFMediatorExample.client.util.comboBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.Pair;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiPredicate;

public class ComboBoxHelper {
    private final List<Pair<ComboBox<?>, String>> comboBoxMappings = new ArrayList<>();
    private final Map<String, BiPredicate<String, Object>> autoCompleteMappings = new HashMap<>();

    public void addMapping(ComboBox<?> comboBox, String feature, BiPredicate<String, Object> autoCompleteLogic) {
        comboBoxMappings.add(new Pair<>(comboBox, feature));
        autoCompleteMappings.put(feature, autoCompleteLogic);
    }

    public void loadDataAndSetItems(List<?> items, List<String> featureNames, Class<?> clazz) {
        Map<String, ObservableList<?>> data = loadData(items, featureNames, clazz);
        setItems(data);
        setAutoComplete();
    }

    private Map<String, ObservableList<?>> loadData(List<?> items, List<String> featureNames, Class<?> clazz) {
        Map<String, Set<Object>> dataSets = new HashMap<>();

        for (String feature : featureNames) {
            dataSets.put(feature, new HashSet<>());
        }

        for (Object item : items) {
            for (String feature : featureNames) {
                try {
                    Method method = clazz.getMethod("get" + capitalize(feature));
                    Object value = method.invoke(item);
                    dataSets.get(feature).add(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Map<String, ObservableList<?>> data = new HashMap<>();
        for (String feature : featureNames) {
            data.put(feature, FXCollections.observableArrayList(dataSets.get(feature)));
        }

        return data;
    }

    private void setItems(Map<String, ObservableList<?>> data) {
        for (Pair<ComboBox<?>, String> mapping : comboBoxMappings) {
            mapping.getKey().setItems((ObservableList) data.get(mapping.getValue()));
        }
    }

    private void setAutoComplete() {
        for (Pair<ComboBox<?>, String> mapping : comboBoxMappings) {
            BiPredicate<String, Object> predicate = autoCompleteMappings.get(mapping.getValue());
            if (predicate != null) {
                il.cshaifasweng.OCSFMediatorExample.client.util.comboBox.AutocompleteComboBox.autoCompleteComboBoxPlus(mapping.getKey(), (typedText, itemToCompare) -> predicate.test(typedText, itemToCompare));
            }
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
