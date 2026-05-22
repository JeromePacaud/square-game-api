package com.jerome.squaregamesapi.catalog;

import java.util.Collection;
import java.util.Locale;

public interface GameCatalog {
    Collection<String> getGameNames(Locale locale);
}
