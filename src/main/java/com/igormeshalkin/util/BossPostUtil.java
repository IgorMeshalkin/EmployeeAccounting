package com.igormeshalkin.util;

import com.igormeshalkin.dao.PostDao;

public class BossPostUtil {
    private static PostDao postDao = new PostDao();

    public static final String BOSS_POST_NAME = "Начальник отдела";
    public static final Long BOSS_POST_ID = postDao.findByName(BOSS_POST_NAME).getId();
}
