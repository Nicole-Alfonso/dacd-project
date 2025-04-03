package org.example.application;

import org.example.domain.model.Route;

import java.util.List;

public interface RouteProvider {
    List<Route> provide();
}
