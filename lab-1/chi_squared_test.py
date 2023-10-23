from scipy.stats import chi2


def chi_squared_test(
        frequencies,
        theoretical_density,
        size_of_sample,
        degrees_of_freedom,
        significance_level=0.05
):
    """
    Perform a chi-squared test for a given sample of values and theoretical density.
    :param frequencies: A list of observed frequencies in different intervals.
    :param theoretical_density: A list of expected theoretical densities corresponding to the intervals.
    :param size_of_sample: The total size of the sample.
    :param degrees_of_freedom: The degrees of freedom for the chi-squared test.
    :param significance_level: The significance level for the chi-squared test. Defaults to 0.05.
    :return: A tuple containing the chi-squared statistic and the critical chi-squared value at the given
             significance level.
    """
    chi_squared = 0
    for i in range(len(frequencies)):
        chi_squared += ((frequencies[i] - theoretical_density[i] * size_of_sample) ** 2) / (
                theoretical_density[i] * size_of_sample)
    chi_squared_critical = chi2.ppf(1 - significance_level, degrees_of_freedom)
    return chi_squared, chi_squared_critical


def check_sample_distribution_law(
        distribution_name,
        frequencies,
        theoretic_density,
        size_of_sample,
        degrees_of_freedom
):
    """
    This function is a wrapper that calculates the chi-squared statistic and the critical value for the chi-squared
    test, and then prints the results. It also prints whether the hypothesis about the distribution is accepted or
    rejected based on the test results.
    """
    chi_squared, chi_squared_critical = chi_squared_test(
        frequencies,
        theoretic_density,
        size_of_sample,
        degrees_of_freedom
    )
    print(
        f'Значення χ-квадрат: {chi_squared}'
        f'\nКритичне значення: {chi_squared_critical}'
    )
    if chi_squared < chi_squared_critical:
        print(f'Гіпотеза про {distribution_name} розподіл приймається')
    else:
        print(f'Гіпотеза про {distribution_name} розподіл відхиляється')
