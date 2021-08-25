package com.ztiany.recyclerview.diff_util;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-25 16:30
 */
public class DataSource {

    private static final int DRAWABLE_COUNT = 15;

    private static final Random random = new Random();

    private static final String[] STRINGS = {
            "原子弹	A-Bomb	理查德·琼斯（Richard Milhouse Rick Jones）",
            "天使	Angel / Archangel	华伦·肯尼思·沃辛顿三世（Warren Kenneth Worthington III）",
            "海妖	Banshee	西恩·卡西迪（Sean Cassidy）",
            "野兽	Beast	汉克·麦考伊（Hank McCoy）",
            "主教	Bishop	卢卡斯·毕希普（Lucas Bishop）",
            "甲虫（I） / 马赫V*	Beetle / MACH-V	艾伯纳·詹金斯（Abner Ronald Abe Jenkins）",
            "黑蝠王	Black Bolt	布莱克卡德·伯特刚（Blackagar Boltagon）",
            "锁齿狗	Lockjaw	史巴克（Sparky）",
            "冰雪暴	Blizzard	葛瑞格·莎潘卡（Gregor Shapanka）",
            "托罗	Toro	汤姆斯·雷德蒙（Thomas Raymond）",
            "黑猫	Black Cat	费莉西亚·哈蒂（Felicia Hardy）",
            "地狱猫	Hellcat	派翠西亚·沃克（Patricia Patsy Walker）",
            "黑豹 (II)	Black Panther	特查拉（T'Challa）",
            "黑骑士 (III)	Black Knight	戴恩·惠特曼（Dane Whitman）",
            "黑寡妇（I）*	Black Widow	娜塔莎·罗曼诺夫（Natalia Alianovna Natasha Romanova）",
            "刀锋战士	Blade	埃里克·布鲁克斯（Eric Brooks）",
            "冬日战士	Bucky / Winter Soldier	詹姆斯·巴恩斯（James Buchanan Bucky Barnes）",
            "机堡	Cable	纳森·桑默斯（Nathan Christopher Charles Summers）",
            "美国队长（I） / 超级士兵	Captain America / Super-Soldier	史蒂芬·罗杰斯（Steven Grant Steve Rogers）",
            "钢人	Colossus	皮特·尼古拉耶维奇·拉斯普廷（Piotr Peter Nikolaievitch Rasputin）",
            "镭射眼	Cyclops	史考特·桑默斯（Scott Summers）",
            "星爵	Star-Lord	彼德·奎尔（Peter Quill）",
            "亚当术士	Adam Warlock	他（Him）",
            "伊格瑞斯	Ikaris	伊格瑞斯（Ikaris）",
            "夜魔侠	Daredevil	马特·梅铎（Matthew Michael Matt Murdock）",
            "死侍*	Deadpool	韦德·威尔逊（Wade Winston Wilson）",
            "奇异博士	Doctor Strange	斯蒂芬·史传奇（Stephen Steve Vincent Strange）",
            "艾丽卡	Elektra	艾丽卡·纳崔斯（Elektra Natchios）",
            "艾玛·佛斯特*	Emma Frost	艾玛·葛蕾丝·佛斯特（Emma Grace Frost）",
            "猎鹰	Falcon	山谬·汤玛斯“山姆”威尔森（Samuel Thomas Sam Wilson）",
            "夜鹰*	Nighthawk	凯尔·李察曼德（Kyle Richmond）",
            "洛克希德	Lockheed	洛克希德（Lockheed）",
            "牌王	Gambit	雷米·乐博（Remy LeBeau）",
            "恶灵骑士 (II)	Ghost Rider	强尼·布雷泽（Johnathon Johnny Blaze）",
            "恶灵骑士 (III)	Ghost Rider	丹尼尔·凯契（Daniel Dan Ketch）",
            "冲击波	Havok	艾力克斯·桑默斯（Alexander Alex Summers）",
            "战迹	Warpath	乔纳森·傲星（James Jonathan Proudstar）",
            "墨纹	Ink	埃里克·吉特（Eric Gitter）",
            "冲蚀	Washout	约翰·罗培兹（John Lopez）",
            "鹰眼（I）*	Hawkeye	克林特·巴顿（Clinton Francis Clint Barton）"};


    public static List<TestBean> getDataSource(Context context) {
        TestBean testBean;
        List<TestBean> source = new ArrayList<>();
        for (int i = 1; i <= DRAWABLE_COUNT; i++) {
            source.add(testBean = new TestBean());
            testBean.setId(i);
            testBean.setDes(STRINGS[random.nextInt(STRINGS.length)]);
            testBean.setDrawableId(context.getResources().getIdentifier("head_portrait" + i, "drawable", context.getPackageName()));
        }
        return source;
    }

    public static int randomDrawable(Context context) {
        return context.getResources().getIdentifier("head_portrait" + random.nextInt(DRAWABLE_COUNT), "drawable", context.getPackageName());
    }

    public static String randomDes() {
        return STRINGS[random.nextInt(STRINGS.length)];
    }

    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    public static int randomInt(int max) {
        return random.nextInt(max);
    }

}