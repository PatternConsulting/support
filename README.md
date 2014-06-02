# Pattern Support

[![Build Status](https://travis-ci.org/PatternConsulting/support.svg?branch=master)](https://travis-ci.org/PatternConsulting/support)

Common utility classes used by Pattern, and possibly helpful to others. This library is in its infancy, and will grow over time. For the moment, it's probably not useful outside Pattern's own projects.

## Conventions

Most of this project serves to augment the capabilities of third-party libraries. Rather than prefix everything within Pattern's namespace, `nu.pattern`, the package structure here is designed to reflect those projects to make obvious what's being augmented. While this runs the risk of polluting those namespaces, inclusion of this library implies an explicit desire for its capabilities. Care is taken to ensure collisions are avoided, and common conventions are kept separate.
