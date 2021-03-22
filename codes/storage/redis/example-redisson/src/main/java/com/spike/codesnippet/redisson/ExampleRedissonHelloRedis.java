package com.spike.codesnippet.redisson;

import java.util.List;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.redisson.config.Config;

import com.google.common.collect.Lists;

/**
 * <pre>
 * see "Hello Redis" in "Redis in Action".
 * 
 * articleId: STRING, AtomicLong
 * article: HASH, Article
 * vote: SET, "user:xxx"
 * score: ZSET, "article:xxx"
 * time: ZSET, "article:xxx"
 * </pre>
 */
public class ExampleRedissonHelloRedis {

  public static void main(String[] args) {
    // 创建客户端
    Config config = new Config();
    config.useSingleServer().setAddress("redis://127.0.0.1:6379");
    final RedissonClient redissonClient = Redisson.create(config);

    ExampleRedissonHelloRedis exampleHelloRedis = new ExampleRedissonHelloRedis();

    String user1 = "Alice";
    String user2 = "Bob";

    Article article1 = new Article();
    article1.title = "article1 title";
    article1.link = "article1 link";
    article1.poster = user1;
    article1.time = new Date();

    exampleHelloRedis.postArticle(redissonClient, article1);
    exampleHelloRedis.voteArticle(redissonClient, user2, 1L);

    List<Article> articles = exampleHelloRedis.queryArticle(redissonClient, 1);
    for (Article article : articles) {
      System.out.println(article);
    }

    try {
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static class Article {
    public Long id; // 被设置
    public String title;
    public String link;
    public String poster;
    public Date time;
    public int votes; // 被设置

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Article [id=");
      builder.append(id);
      builder.append(", title=");
      builder.append(title);
      builder.append(", link=");
      builder.append(link);
      builder.append(", poster=");
      builder.append(poster);
      builder.append(", time=");
      builder.append(time);
      builder.append(", votes=");
      builder.append(votes);
      builder.append("]");
      return builder.toString();
    }
  }

  void postArticle(RedissonClient redissonClient, Article article) {
    RAtomicLong ral = redissonClient.getAtomicLong("articleId");
    article.id = ral.addAndGet(1L);

    // article:
    String articleKey = "article:" + article.id;
    RMap<String, Object> articleMap = redissonClient.<String, Object> getMap(articleKey);
    articleMap.put("title", article.title);
    articleMap.put("link", article.link);
    articleMap.put("poster", article.poster);
    articleMap.put("time", article.time.getTime());
    articleMap.put("votes", 1);

    // vote:
    String voteKey = "vote:" + article.id;
    RSet<String> voteSet = redissonClient.<String> getSet(voteKey);
    voteSet.add("user:" + article.poster);
    voteSet.expire(7, TimeUnit.DAYS);

    // score:
    RScoredSortedSet<String> scoreZSet = redissonClient.<String> getScoredSortedSet("score:");
    scoreZSet.add(article.time.getTime() + 432, "article:" + article.id);

    // time:
    RScoredSortedSet<String> timeZSet =
        redissonClient.<String> getScoredSortedSet("time:" + article.id);
    timeZSet.add(article.time.getTime(), "article:" + article.id);
  }

  void voteArticle(RedissonClient redissonClient, String userId, Long articleId) {
    RSet<String> voteSet = redissonClient.<String> getSet("vote:" + articleId);
    if (!voteSet.contains("user:" + userId)) {
      voteSet.add("user:" + userId);
    }

    RScoredSortedSet<String> scoreZSet = redissonClient.<String> getScoredSortedSet("score:");
    scoreZSet.addAndGetRank(432, "article:" + articleId);

    RMap<String, Object> articleMap =
        redissonClient.<String, Object> getMap("article:" + articleId);
    int votes = (int) articleMap.get("votes");
    articleMap.put("votes", votes + 1);
  }

  final int ARTICLE_PAGE_SIZE = 25;

  List<Article> queryArticle(RedissonClient redissonClient, int page) {
    List<Article> result = Lists.newArrayList();

    int start = (page - 1) * ARTICLE_PAGE_SIZE;
    int end = start + ARTICLE_PAGE_SIZE - 1;

    RScoredSortedSet<String> scoreZSet = redissonClient.<String> getScoredSortedSet("score:");
    Collection<ScoredEntry<String>> articleIds = scoreZSet.entryRange(start, end);
    for (ScoredEntry<String> articleId : articleIds) {
      RMap<String, Object> articleMap =
          redissonClient.<String, Object> getMap(articleId.getValue());
      Article article = new Article();
      article.id = Long.valueOf(articleId.getValue().substring("article:".length()));
      article.title = (String) articleMap.get("title");
      article.link = (String) articleMap.get("link");
      article.poster = (String) articleMap.get("poster");
      article.time = new Date((Long) articleMap.get("time"));
      article.votes = (int) articleMap.get("votes");
      result.add(article);
    }

    return result;
  }

}
