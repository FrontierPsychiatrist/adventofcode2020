package adventofcode2020;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

record Passport(String birthyear, String issueyear, String expirationyear, String height,
                String haircolor, String eyecolor, String pid, String cid) {

    private static final Set<String> EYE_COLORS = new HashSet<>(7);
    private static final Pattern HAIRCOLOR = Pattern.compile("#[0-9a-f]{6}");
    private static final Pattern PID = Pattern.compile("\\d{9}");


    static {
        EYE_COLORS.add("amb");
        EYE_COLORS.add("blu");
        EYE_COLORS.add("brn");
        EYE_COLORS.add("gry");
        EYE_COLORS.add("grn");
        EYE_COLORS.add("hzl");
        EYE_COLORS.add("oth");
    }

    public boolean isValid() {
        return birthyear != null && issueyear != null && expirationyear != null && height != null
                && haircolor != null && eyecolor != null && pid != null;
    }

    public boolean isValidPart2() {
        var valid = true;

        if (birthyear == null) {
            return false;
        }
        try {
            var v = Integer.parseInt(birthyear);
            valid = v >= 1920 && v <= 2020;
        } catch (NumberFormatException e) {
            return false;
        }

        if (issueyear == null) {
            return false;
        }

        try {
            var v = Integer.parseInt(issueyear);
            valid = valid && v >= 2010 && v <= 2020;
        } catch (NumberFormatException e) {
            return false;
        }

        if (expirationyear == null) {
            return false;
        }
        try {
            var v = Integer.parseInt(expirationyear);
            valid = valid && v >= 2020 && v <= 2030;
        } catch (NumberFormatException e) {
            return false;
        }

        if (height == null) {
            return false;
        }

        if (height.endsWith("in")) {
            var i = height.indexOf('i');
            var v = Integer.parseInt(height.substring(0, i));
            valid = valid && v >= 59 && v <= 76;
        } else if (height.endsWith("cm")) {
            var i = height.indexOf('c');
            var v = Integer.parseInt(height.substring(0, i));
            valid = valid && v >= 150 && v <= 193;
        } else {
            return false;
        }

        if (haircolor == null) {
            return false;
        }
        valid = valid && HAIRCOLOR.matcher(haircolor).matches();

        if (eyecolor == null) {
            return false;
        }
        valid = valid && EYE_COLORS.contains(eyecolor);

        if (pid == null) {
            return false;
        }
        valid = valid && PID.matcher(pid).matches();

        return valid;
    }

}

public class Day4 {

    public Day4() {
        InputStream input = this.getClass()
                .getClassLoader().getResourceAsStream("day4_passport_processing");
        var reader = new BufferedReader(new InputStreamReader(input));

        var passports = reader.lines()
                .collect(
                        () -> {
                            var list = new ArrayList<Map<String, String>>();
                            list.add(new HashMap<>());
                            return list;
                        },
                        (list, next) -> {
                            if (next.equals("")) {
                                list.add(new HashMap<>());
                            } else {
                                var current = list.get(list.size() - 1);
                                Arrays.stream(next.split(" ")).forEach(t -> {
                                    var components = t.split(":");
                                    current.put(components[0], components[1]);
                                });
                            }
                        },
                        ArrayList::addAll
                )
                .stream()
                .map(map ->
                        new Passport(
                                map.get("byr"),
                                map.get("iyr"),
                                map.get("eyr"),
                                map.get("hgt"),
                                map.get("hcl"),
                                map.get("ecl"),
                                map.get("pid"),
                                map.get("cid")
                        )
                )
                .filter(Passport::isValidPart2)
                .count();

        System.out.println(passports);
    }

    public static void main(String[] args) {
        new Day4();
    }
}
