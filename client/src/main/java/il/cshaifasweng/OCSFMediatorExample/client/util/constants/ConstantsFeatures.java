package il.cshaifasweng.OCSFMediatorExample.client.util.constants;

import java.util.Arrays;
import java.util.List;

public class ConstantsFeatures {
    public static class MovieConstants {
        public static final String HEBREW = "hebrewName";
        public static final String DURATION = "duration";
        public static final String ENGLISH = "englishName";
        public static final String GENRE = "genre";
        public static final String ID = "id";
        public static final String THEATER_PRICE = "theaterPrice";
        public static final String STREAMING_TYPE = "streamingType";
        public static final String HV_PRICE = "homeViewingPrice";

        public static final List<String> FEATURE_NAMES = Arrays.asList(
                HEBREW, DURATION, ENGLISH, GENRE, ID, THEATER_PRICE, STREAMING_TYPE, HV_PRICE
        );
    }

    public static class AnotherConstants {
        public static final String ATTRIBUTE1 = "attribute1";
        public static final String ATTRIBUTE2 = "attribute2";

        public static final List<String> FEATURE_NAMES = Arrays.asList(
                ATTRIBUTE1, ATTRIBUTE2
        );
    }
}
