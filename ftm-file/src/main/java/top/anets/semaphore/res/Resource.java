package top.anets.semaphore.res;

import lombok.Data;

@Data
class Resource {
    private int number;
    private String name;

    public Resource() {
        number = 0;
    }

    public Resource(int number) {
        this.number = number;
    }

    public Resource(int number, String name) {
        this.number = number;
        this.name = name;
    }
}