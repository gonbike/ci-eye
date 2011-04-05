package org.netmelody.cieye.core.domain;

import static com.google.common.collect.Iterators.find;
import static com.google.common.collect.Iterators.transform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public final class Target {

    private final String id;
    private final String webUrl;
    private final String name;
    private final Status status;
    private final long lastStartTime;
    private final List<Sponsor> sponsors = new ArrayList<Sponsor>();
    private final List<Build> builds = new ArrayList<Build>();
    
    public Target(String id, String webUrl, String name, Status status) {
        this(id, webUrl, name, status, 0L, new ArrayList<Build>(), new ArrayList<Sponsor>());
    }
    
    public Target(String id, String webUrl, String name, Status status, long lastStartTime, List<Build> builds, List<Sponsor> sponsors) {
        this.id = id;
        this.webUrl = webUrl;
        this.name = name;
        this.lastStartTime = lastStartTime;
        this.sponsors.addAll(new HashSet<Sponsor>(sponsors));
        this.builds.addAll(builds);
        
        this.status = find(transform(builds.iterator(), new Function<Build, Status>() {
                                                            @Override public Status apply(Build build) {
                                                                return build.status();
                                                            }
                                                        }),
                           new Predicate<Status>() {
                               @Override public boolean apply(Status status) {
                                   return Status.BROKEN.equals(status);
                               }
                           },
                           status);
    }

    public String id() {
        return id;
    }
    
    public String name() {
        return name;
    }
    
    public Status status() {
        return status;
    }
    
    public long lastStartTime() {
        return lastStartTime;
    }
    
    public List<Sponsor> sponsors() {
        return new ArrayList<Sponsor>(sponsors);
    }

    public List<Build> builds() {
        return new ArrayList<Build>(builds);
    }

    public Target withBuilds(List<Build> builds) {
        return new Target(id, webUrl, name, status, lastStartTime, builds, sponsors);
    }

    public Target withStatus(Status newStatus) {
        return new Target(id, webUrl, name, newStatus, lastStartTime, builds, sponsors);
    }
}