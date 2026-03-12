# Domain Overview

This folder describes the business domain of NordCargo's Trip Tracker.
It defines the core entities, their responsibilities, and their relationships.

## Core Entities

- [[customer]]
- [[location]]
- [[address]]
- [[order]]
- [[stop]]
- [[trip]]
- [[trip_event]]
- [[document]]
- [[driver]]
- [[vehicle]]
- [[trailer]]

## High-Level Relationships (Business View)

- A [[customer]] places many [[order]]s.
- An [[order]] contains one or more [[stop]]s.
- A [[trip]] executes one or more [[order]]s (MVP may start with 1).
- A [[trip]] is assigned to one [[driver]] and one [[vehicle]] (and optionally one [[trailer]]).
- A [[trip]] has many [[trip_event]]s (append-only timeline).
- A [[trip]] can have many [[document]]s (e.g., POD).
- A [[driver]] / [[customer]] / [[location]] can reference an [[address]].

## Important Notes

- Trip progress is recorded as an append-only event timeline ([[trip_event]]).
- "Current trip status" may be derived from the latest event, but the timeline is the source of truth.
