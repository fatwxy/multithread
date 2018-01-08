package com.yq.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SimpleDemo {

    public static void main(String[] args) {
        ArrayList<String> strList = new ArrayList<> ();
        String[] wordsArray = {"divestiture", "platform agnostic", "agnostic", "spontaneous",
            "rudimentary", "carryover balance", "groovy", "stunning", "amortize", "best-of-breed", "fortuitous", "strikethrough",
            "hospitable", "perceive", "convincing", "envoy", "desperate", "ambiguity"};
        List<String> strList1 = java.util.Arrays.asList(wordsArray);
        strList.addAll(strList1);
        Stream<String> wordStream = strList.stream();

        long current = System.currentTimeMillis();
        Optional<String> startsWithaFirst = wordStream.filter(s -> s.startsWith("a") ).findFirst();
        long after = System.currentTimeMillis();
        System.out.println("start with 'a': " + startsWithaFirst.orElse("") + ", used time:" + (after - current));

        //必须重新生成流，否则就有stream has already been operated upon or closed 错误。
        //Java 8 streams cannot be reused. As soon as you call any terminal operation the stream is closed:
        wordStream = strList.parallelStream();
        current = System.currentTimeMillis();
        Optional<String> startsWithaAny = wordStream.filter(s -> s.startsWith("a") ).findAny();
        after = System.currentTimeMillis();
        System.out.println("start with 'a' any: " + startsWithaAny.orElse("")+ ", used time:" + (after - current));

        final Stream<String> wordStreamFinal = strList.parallelStream();
        Supplier<Stream<String>> streamSupplier = () -> wordStreamFinal.filter(s -> s.startsWith("a"));

        current = System.currentTimeMillis();
        startsWithaFirst = streamSupplier.get().findFirst();
        after = System.currentTimeMillis();
        System.out.println("start with 'a' fist(resue stream): " + startsWithaFirst.orElse("")+ ", used time:" + (after - current));

    }

}
