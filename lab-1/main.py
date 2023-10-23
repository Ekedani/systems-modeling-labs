from generators import generate_n_exponential, generate_n_normal, generate_n_uniform
from chi_squared_test import check_sample_distribution_law
from scipy.stats import expon, norm, uniform
from statistics import find_sample_mean_and_variance, get_frequency_table, merge_intervals, build_histogram

if __name__ == '__main__':
    # Exponential distribution
    lambdas = [0.05, 0.5, 0.8, 1.5]
    SAMPLE_SIZE = 10000
    law_name = 'експоненційний'

    for lamda in lambdas:
        parameters = f'λ = {lamda}'
        print(f'========== Перевірка для {parameters}, {law_name} розподіл ==========')
        exponential_sample = generate_n_exponential(SAMPLE_SIZE, lamda)

        # Task 2
        find_sample_mean_and_variance(exponential_sample)
        intervals, frequencies = get_frequency_table(exponential_sample)
        merged_intervals, merged_frequencies = merge_intervals(intervals, frequencies)
        theoretic_density = []
        for i in range(len(merged_intervals) - 1):
            theoretic_density.append(
                expon.cdf(merged_intervals[i + 1], scale=1 / lamda) - expon.cdf(merged_intervals[i], scale=1 / lamda)
            )

        build_histogram(
            exponential_sample,
            intervals,
            theoretic_density,
            merged_intervals,
            law_name,
            parameters
        )

        # Task 3
        degrees_of_freedom = len(merged_frequencies) - 1 - 1
        check_sample_distribution_law(
            law_name,
            merged_frequencies,
            theoretic_density,
            SAMPLE_SIZE,
            degrees_of_freedom
        )

    # Normal distribution
    sigmas = [10, 20, 40, 50]
    alphas = [5, 40, 100, 200]
    law_name = 'нормальний'
    for sigma_alpha in zip(sigmas, alphas):
        parameters = f'σ = {sigma_alpha[0]}, α = {sigma_alpha[1]}'
        print(f'========== Перевірка для {parameters}, {law_name} розподіл ==========')
        normal_sample = generate_n_normal(SAMPLE_SIZE, sigma_alpha[0], sigma_alpha[1])

        # Task 2
        find_sample_mean_and_variance(normal_sample)
        intervals, frequencies = get_frequency_table(normal_sample)
        merged_intervals, merged_frequencies = merge_intervals(intervals, frequencies)
        theoretic_density = []
        for i in range(len(merged_intervals) - 1):
            theoretic_density.append(
                norm.cdf(merged_intervals[i + 1], loc=sigma_alpha[1], scale=sigma_alpha[0]) -
                norm.cdf(merged_intervals[i], loc=sigma_alpha[1], scale=sigma_alpha[0])
            )

        build_histogram(
            normal_sample,
            intervals,
            theoretic_density,
            merged_intervals,
            law_name,
            parameters
        )

        # Task 3
        degrees_of_freedom = len(merged_frequencies) - 1 - 2
        check_sample_distribution_law(
            law_name,
            merged_frequencies,
            theoretic_density,
            SAMPLE_SIZE,
            degrees_of_freedom
        )

    # Uniform distribution
    a_values = [5 ** 13, 7 ** 5, 10 ** 10, 95]
    c_values = [2 ** 31, 2 ** 12, 42 ** 10, 108]
    law_name = 'рівномірний'
    for a_c in zip(a_values, c_values):
        parameters = f'a = {a_c[0]}, c = {a_c[1]}'
        print(f'========== Перевірка для {parameters}, {law_name} розподіл ==========')
        uniform_sample = generate_n_uniform(SAMPLE_SIZE, a_c[0], a_c[1])

        # Task 2
        find_sample_mean_and_variance(uniform_sample)
        intervals, frequencies = get_frequency_table(uniform_sample)
        merged_intervals, merged_frequencies = merge_intervals(intervals, frequencies)
        theoretic_density = []
        for i in range(len(intervals) - 1):
            theoretic_density.append(
                uniform.cdf(intervals[i + 1], loc=0, scale=1) -
                uniform.cdf(intervals[i], loc=0, scale=1)
            )

        build_histogram(
            uniform_sample,
            intervals,
            theoretic_density,
            intervals,
            law_name,
            parameters
        )

        theoretic_density = []
        for i in range(len(merged_intervals) - 1):
            theoretic_density.append(
                uniform.cdf(merged_intervals[i + 1], loc=0, scale=1) -
                uniform.cdf(merged_intervals[i], loc=0, scale=1)
            )

        # Task 3
        degrees_of_freedom = len(merged_frequencies) - 1 - 2
        if degrees_of_freedom < 0:
            print('Занадто мала кількість ступенів свободи, перевірка не може бути проведена')
            continue
        check_sample_distribution_law(
            law_name,
            merged_frequencies,
            theoretic_density,
            SAMPLE_SIZE,
            degrees_of_freedom
        )
