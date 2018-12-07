package net.iakovlev.timeshape.benchmarks;

import net.iakovlev.timeshape.TimeZoneEngine;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;

public class PolylineQueryBenchmark {
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        double latStart = 52.52;
        double lonStart = 13.40;
        double latEnd = 56.52;
        double lonEnd = 16.40;

        int steps = 200;

        double latStep = Math.abs(latEnd - latStart) / steps;
        double lonStep = Math.abs(lonEnd - lonStart) / steps;
        double[] points;
        TimeZoneEngine engine = TimeZoneEngine.initialize();
        ArrayList<Double> pointsList = new ArrayList<>();

        @Setup
        public void setup() {
            for (int i = 0; i < steps; i++) {
                pointsList.add(latStart + latStep * i);
                pointsList.add(lonStart + lonStep * i);
            }

            points = pointsList.stream().mapToDouble(Double::doubleValue).toArray();
        }
    }


    @Benchmark
    public void testOldAPI(BenchmarkState state, Blackhole blackhole) {
        for (int i = 0; i < state.points.length - 1; i += 2) {
            blackhole.consume(state.engine.query(state.points[i + 1], state.points[i]));
        }
    }

//    @Benchmark
    public void testNewAPI(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.engine.query(state.points));
    }

//    @Benchmark
    public void testNewAPI1(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.engine.query1(state.points));
    }

    @Benchmark
    public void testNewAPI2(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.engine.query2(state.points));
    }

    @Benchmark
    public void testNewAPI3(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.engine.query3(state.points));
    }
}
