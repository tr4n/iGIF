package com.example.mypc.officaligif.models;

import android.support.annotation.NonNull;

public class PairModel implements Comparable<PairModel> {
    public int first;
    public int second;

    public PairModel(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public PairModel() {
        this.first = this.second = 0;
    }

    @Override
    public int compareTo(@NonNull PairModel o) {
        return this.first == o.first
                ? this.second == o.second ? 0
                : this.second - o.second
                : this.first - o.first;
    }
}
