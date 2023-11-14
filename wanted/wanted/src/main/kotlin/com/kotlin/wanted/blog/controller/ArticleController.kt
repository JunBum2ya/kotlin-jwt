package com.kotlin.wanted.blog.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/articles")
@Controller
class ArticleController {

    @GetMapping
    fun articles() : String {
        return "articles/index"
    }

}