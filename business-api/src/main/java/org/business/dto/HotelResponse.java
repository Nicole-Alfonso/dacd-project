package org.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HotelResponse {

    @JsonProperty("total_hoteles")
    private int totalHoteles;

    @JsonProperty("hoteles")
    private List<HotelDTO> hoteles;

    public HotelResponse(int totalHoteles, List<HotelDTO> hoteles) {
        this.totalHoteles = totalHoteles;
        this.hoteles = hoteles;
    }

    public int getTotalHoteles() { return totalHoteles; }
    public List<HotelDTO> getHoteles() { return hoteles; }
}
