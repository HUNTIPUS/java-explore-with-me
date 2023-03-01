package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsDtoOutput implements Comparable<StatsDtoOutput> {

    private String app;
    private String uri;
    private long hits;

    @Override
    public int compareTo(StatsDtoOutput o) {
        return (int) (o.getHits() - this.getHits());
    }
}
