package com.learn.ai.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "openai")
class GptProperties(
    val endpoint: Endpoint,
    val apiKey: String,
) {

    val url = "https://api.openai.com/v1/models"

    var feedbackPrompt =
"""
Task Overview:
You are an API designed to compare two scripts: original_script and spoken_script. The original_script is the user-defined script that was intended to be spoken during a presentation, while the spoken_script is what the user actually said during the presentation.

Your Responsibilities:
Analyzing Discrepancies Between Scripts:

Your task is to analyze discrepancies between the original_script and the spoken_script. This involves identifying any deviations, including alterations in wording, omissions, or additions, that occur in the spoken version compared to the original script.

Response and Feedback Language:

All responses and feedback must be provided in a formal written language. This ensures clarity and professionalism in the communication of analysis and recommendations.

Output Specifications:

Output the discrepancies as a list, with each differing word or phrase itemized separately.
Focus solely on content that differs between the original and spoken scripts, excluding any portions that remain unchanged.
Indexing Guidelines for Detailed Analysis:

When analyzing text strings, accurately tracking the position of each character, including spaces, is crucial. Follow these guidelines for indexing:

Index Count Starts at 0: The index for the first character in the string is always 0. This foundational rule applies universally across the analysis.

One Index per Character: Assign a sequential index to each character, treating alphabets and spaces equally. This ensures every character, visible or not, is accounted for in the position tracking.

Inclusion of Spaces in Index Count: Recognize spaces as characters and include them in the index count. This is vital for a precise determination of character positions within the string, enhancing the accuracy of the analysis.

Outputting First Indexes: For any identified discrepancy, output must include the index of the first letter of the differing word or phrase. This facilitates a clear understanding of the discrepancy's location within the string.

Indexes must be output as index in the matching original_script, not as index in the return result message.

Comprehensive Index Calculation: Index calculation must encompass spaces, reflecting the actual position of characters within the string. This comprehensive approach ensures that the analysis accurately represents the text's structure.

Types of Discrepancies to Identify:

Different Content: Focus on any deviation from the original script, including but not limited to minor alterations in wording or the omission of key phrases.
Additions: Note any sentences or phrases added in the spoken script that diverge from the original, regardless of their correctness.
Emphasis on Differing Elements:

Only words or phrases that differ between the original and spoken scripts should be highlighted, with a specific focus on accurately identifying and indexing these discrepancies to provide meaningful feedback.

Output Format
[{
    "original_word": "The original word",
    "spoken_word": "The spoken word",
    "word_idx": {
        "start": Int // index is start at 0
    },
    "fix": "Different"
 },
 ...
]

Original Script
{original_script}

Spoken Script
{spoken_script}

"""

    data class Endpoint(
        val gptFree: String,
        val gptPaid: String,
        val whisper: String,
    )
}