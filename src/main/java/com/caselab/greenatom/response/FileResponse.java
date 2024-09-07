package com.caselab.greenatom.response;

import java.time.ZonedDateTime;

public record FileResponse(String title, ZonedDateTime creation_date, String description) {}
