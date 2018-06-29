package com.konselorku.android.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeModel {
    public class PostJson {
        @SerializedName("posts")
        public List<Hasil> posts;

        public class Hasil {
            @SerializedName("url")
            public String url;

            @SerializedName("id")
            public String id;

            @SerializedName("title")
            public String title;

            @SerializedName("excerpt")
            public String excerpt;

            @SerializedName("thumbnail_images")
            public MediumLarge thumbnail_images;

            @SerializedName("content")
            public String content;

            @SerializedName("author")
            public Author author;

            @SerializedName("date")
            public String date;

            @SerializedName("comment_count")
            public String comment_count;

            @SerializedName("categories")
            public List<Kategori> categories;

        }

        public class MediumLarge {
            @SerializedName("medium_large")
            public UrlThumbnail medium_large;
        }

        public class UrlThumbnail {
            @SerializedName("url")
            public String urlThumbnail;
        }

        public class Author {
            @SerializedName("name")
            public String name;
        }

        public class Kategori {
            @SerializedName("title")
            public String title;

            @SerializedName("post_count")
            public String post_count;
        }
    }

    public class KategoriPost {
        @SerializedName("categories")
        public List<Hasil> categories;

        public class Hasil {
            @SerializedName("slug")
            public String slug;

            @SerializedName("title")
            public String title;

            @SerializedName("post_count")
            public String post_count;
        }
    }

    public class KomentarPost {

        @SerializedName("comments")
        public List<Komentar> comments;

        public class Komentar {
            @SerializedName("name")
            public String name;

            @SerializedName("content")
            public String content;
        }
    }
}
