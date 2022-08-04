import java.util.*;

/*
    In this class we do these normalizations:
    1. removing numbers
    2. removing arabic characters
    3. stemming
    4. removing all punctuations
    5. removing some frequent persian postfix
    6. removing some meaningless words

    we removing frequent terms with a ratio; if each term has more frequencies of this ratio of doc count will delete
 */
public class Tokenizer
{
    private final String splitDelimiter = " ";
    private final HashMap<String, List<String>> stemList = new HashMap<>()
    {
        {
            put("رفتن", new ArrayList<>(Arrays.asList("رفت", "رفتی", "میرود", "میرفت", "میاید", "رفتن", "رفتند", "میروم", "میروی", "میرویم", "میروند")));
            put("بردن", new ArrayList<>(Arrays.asList("برد", "بردند", "میبرند", "میبریم", "میبرد", "میاورد", "بردیم", "بردم", "بردی", "بردید", "میبرید")));
            put("پذیرفتن", new ArrayList<>(Arrays.asList("پذیرش", "پذیرفت", "پذیرفتند", "پذیرفتن", "میپذیرد", "میپذیرند", "بپذیر", "میپذیریم", "میپذیرم")));
            put("نوشتن", new ArrayList<>(Arrays.asList("نوشت", "نوشتند", "مینویسد", "بنویس", "بنویسند", "نوشتیم", "مینویسیم")));
        }
    };
    private final HashMap<String, String> arabicCharacters = new HashMap<>()
    {
        {
            put("ك", "ک");
            put("ئ", "ی");
            put("ي", "ی");
            put("آ", "ا");
            put("أ", "ا");
            put("ة", "ه");
            put("ۀ", "ه");
            put("ؤ", "و");
            put("ء", "");
        }
    };
    private final List<String> postFixList = new ArrayList<>(Arrays.asList("های", "ها", "ترین", "تری", "تر", "مان", "ات", "ان", "مین"));
    private final List<String> meaninglessList = new ArrayList<>(Arrays.asList("برای", "از", "او", "همه", "ها", "هی", "آها", "های", "دیگر"));

    public List<String> tokenize(String data)
    {
        List<String> result = new ArrayList<>();
        data = removeHalfSpace(data);
        data = removeWhiteEnter(data);
        data = data.trim();
        for (String token : data.split(splitDelimiter)) {
            token = removeArabicCharacters(token);
            token = removePostfix(token);
            token = removeMeaninglessWords(token);
            token = removeSpecialChars(token);
            token = stemming(token);
            if (token.length() > 2)
            {
                result.add(token.trim());
            }
        }
        return result;
    }

    private String removeHalfSpace(String data)
    {
        return data.replaceAll("\u200c", " ");
    }

    private String removeWhiteEnter(String data)
    {
        return data.replaceAll("\n", " ");
    }

    private String removeSpecialChars(String data) // here we delete persian and english numbers, all punctuations, and all meaningless characters
    {
        return data.replaceAll("[^\\u0622\\u0627\\u0628\\u067E\\u062A-\\u062C\\u0686\\u062D-\\u0632\\u0698\\u0633-\\u063A\\u0641\\u0642\\u06A9\\u06AF\\u0644-\\u0648\\u06CCA-Za-z]", "");
    }

    private String removePostfix(String data) // here we remove some frequent postfixes of persian language
    {
        for (String postFix : postFixList)
        {
            if (data.endsWith(postFix))
            {
                return data.replaceAll(postFix, "");
            }
        }
        return data;
    }

    private String stemming(String data)
    {
        for (Map.Entry<String, List<String>> row : stemList.entrySet())
        {
            if (row.getValue().contains(data))
            {
                return row.getKey();
            }
        }
        return data;
    }

    private String removeArabicCharacters(String data)
    {
        for (Map.Entry<String, String> row : arabicCharacters.entrySet())
        {
            data = data.replaceAll(row.getKey(), row.getValue());
        }
        return data;
    }

    private String removeMeaninglessWords(String data)
    {
        for (String word : meaninglessList)
        {
            data = data.replaceAll(word, "");
        }
        return data;
    }
}
