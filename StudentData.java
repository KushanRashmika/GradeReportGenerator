public record StudentData(String name, int maths, int science, int english) {
    double average() {
        return (maths + science + english) / 3.0;
    }
}
