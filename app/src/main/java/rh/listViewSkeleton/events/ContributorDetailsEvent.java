package rh.listViewSkeleton.events;

import rh.listViewSkeleton.model.Contributor;

/**
 * Created by ryanheitner on 23/06/2014.
 */
public class ContributorDetailsEvent {
    public Contributor contributor;

    public ContributorDetailsEvent(Contributor contributor) {
        this.contributor = contributor;
    }
}
