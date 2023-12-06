package io.github.FlyingASea.service;

import io.github.FlyingASea.entity.TaskEntity;
import io.github.FlyingASea.post.RoomPost;
import io.github.FlyingASea.result.ApiException;
import io.github.FlyingASea.result.Errors;
import jakarta.annotation.Resource;
import org.quartz.*;
import org.quartz.ee.jmx.jboss.QuartzService;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

